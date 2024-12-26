package com.wekids.backend.member.service;

import com.wekids.backend.account.domain.Account;
import com.wekids.backend.account.repository.AccountRepository;
import com.wekids.backend.account.service.AccountService;
import com.wekids.backend.admin.dto.request.MemberSimplePasswordRequest;
import com.wekids.backend.baas.dto.request.AccountGetRequest;
import com.wekids.backend.baas.dto.request.WekidsRegistrationRequest;
import com.wekids.backend.baas.dto.response.AccountGetResponse;
import com.wekids.backend.baas.dto.response.BankMemberIdResponse;
import com.wekids.backend.baas.service.BaasService;
import com.wekids.backend.design.domain.Design;
import com.wekids.backend.design.domain.enums.CharacterType;
import com.wekids.backend.design.domain.enums.ColorType;
import com.wekids.backend.design.repository.DesignRepository;
import com.wekids.backend.exception.ErrorCode;
import com.wekids.backend.exception.WekidsException;
import com.wekids.backend.util.masking.service.DataMaskingServiceImpl;
import com.wekids.backend.member.domain.Member;
import com.wekids.backend.member.domain.Parent;
import com.wekids.backend.member.domain.enums.MemberState;
import com.wekids.backend.member.dto.request.AccountInquiryAgreeRequest;
import com.wekids.backend.member.dto.request.ParentAccountRegistrationRequest;
import com.wekids.backend.member.dto.response.ChildResponse;
import com.wekids.backend.member.dto.response.ParentAccountResponse;
import com.wekids.backend.member.dto.response.ParentResponse;
import com.wekids.backend.member.dto.response.ParentSimplePasswordRequest;
import com.wekids.backend.member.repository.ChildRepository;
import com.wekids.backend.member.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final AccountRepository accountRepository;
    private final DesignRepository designRepository;
    private final BaasService baasService;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final DataMaskingServiceImpl maskingService;

    @Override
    @Transactional
    public ParentAccountResponse showParentAccount(Long parentId) {
        Parent parent = getParent(parentId);
        Account parentAccount = findAccountByMember(parent);
        Design design = findDesignByMemberId(parentId);

        if(parentAccount != null) accountService.updateAccount(parentAccount);

        ParentResponse parentResponse = ParentResponse.of(parent, parentAccount, design);

        parentResponse.applyMasking(maskingService);

        List<ChildResponse> childResponses = showChildAccount(parentId);

        return ParentAccountResponse.of(parentResponse, childResponses);
    }

    private List<ChildResponse> showChildAccount(Long parentId) {
        return childRepository.findChildrenByParentId(parentId).stream()
                .filter(child -> child.getState().equals(MemberState.ACTIVE))
                .map(child -> {
                    Account childAccount = findAccountByMember(child);
                    Design childDesign = findDesignByMemberId(child.getId());
                    if (childAccount != null) accountService.updateAccount(childAccount);
                    ChildResponse childResponse = ChildResponse.of(child, childAccount, childDesign);
                    childResponse.applyMasking(maskingService);
                    return childResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void agreeAccountInquiry(AccountInquiryAgreeRequest accountInquiryAgreeRequest, Long memberId) {
        Parent parent = getParent(memberId);

        WekidsRegistrationRequest wekidsRegistrationRequest = WekidsRegistrationRequest.of(parent.getName(), parent.getBirthday(), accountInquiryAgreeRequest.getResidentRegistrationNumber());

        BankMemberIdResponse response = baasService.registerWekids(wekidsRegistrationRequest);

        parent.saveBankMemberId(response.getBankMemberId());
    }

    @Override
    @Transactional
    public void registerParentAccount(ParentAccountRegistrationRequest request, Long memberId) {
        Parent parent = getParent(memberId);

        AccountGetRequest accountGetRequest = AccountGetRequest.of(request.getAccountNumber());
        AccountGetResponse accountGetResponse = baasService.getAccount(accountGetRequest);

        Account account = Account.of(accountGetResponse, parent);

        accountRepository.save(account);

        Design design = Design.create(parent, ColorType.BLUE, CharacterType.DADAPING);
        design.updateAccount(account);

        designRepository.save(design);
    }

    @Override
    @Transactional
    public void changeSimplePassword(Long memberId, MemberSimplePasswordRequest request) {
        Parent parent = getParent(memberId);
        String encode = passwordEncoder.encode(request.getNewPassword());
        parent.changeSimplePassword(encode);
    }

    @Override
    @Transactional
    public void changeSimplePassword(Long memberId, ParentSimplePasswordRequest request) {
        Parent parent = getParent(memberId);

        validationSimplePassword(request, parent.getSimplePassword());

        String encode = passwordEncoder.encode(request.getNewPassword());
        parent.changeSimplePassword(encode);
    }

    private void validationSimplePassword(ParentSimplePasswordRequest request, String encodedPassword){
        if(request.getRawPassword().equals(request.getNewPassword())){
            throw new WekidsException(ErrorCode.FAILED_UPDATE_PASSWORD,  "기존 비밀번호와 변경할 비밀반호가 일치합니다.");
        }

        if(!passwordEncoder.matches(request.getRawPassword(), encodedPassword)){
            throw new WekidsException(ErrorCode.NOT_MATCHED_PASSWORD, request.getRawPassword() + "는 틀린 비밀번호입니다.");
        }
    }

    private Parent getParent(Long memberId) {
        return parentRepository.findById(memberId).orElseThrow(() -> new WekidsException(ErrorCode.MEMBER_NOT_FOUND, "회원 아이디: " + memberId));
    }

    private Account findAccountByMember(Member member) {
        return accountRepository.findByMember(member).orElse(null);
    }

    private Design findDesignByMemberId(Long parentId) {
        return designRepository.findById(parentId).orElse(null);
    }
}
