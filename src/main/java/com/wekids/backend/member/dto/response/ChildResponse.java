package com.wekids.backend.member.dto.response;

import com.wekids.backend.account.domain.Account;
import com.wekids.backend.design.domain.Design;
import com.wekids.backend.design.domain.enums.CharacterType;
import com.wekids.backend.design.domain.enums.ColorType;
import com.wekids.backend.util.masking.service.DataMaskingServiceImpl;
import com.wekids.backend.member.domain.Child;
import com.wekids.backend.member.domain.enums.CardState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChildResponse {
    Long childId;
    String name;
    String accountNumber;
    CharacterType profile;
    String balance;
    Long accountId;
    CardState cardState;
    ColorType color;
    CharacterType character;

    public void applyMasking(DataMaskingServiceImpl maskingService) {
        if(this.name != null) this.name = maskingService.maskData(this.name);
        if(this.accountNumber != null) this.accountNumber = maskingService.maskData(this.accountNumber);
        if(this.balance != null) this.balance = maskingService.maskBalance(new BigDecimal(this.balance));
    }


    public static ChildResponse of(Child child, Account account, Design design) {
        ChildResponse.ChildResponseBuilder builder = ChildResponse.builder()
                .childId(child.getId())
                .name(child.getName())
                .profile(child.getProfile())
                .cardState(child.getCardState());

        if (account != null) {
            builder.accountNumber(account.getAccountNumber())
                    .balance(account.getBalance().toPlainString())
                    .accountId(account.getId());
        }

        if (design != null) {
            builder.color(design.getColor())
                    .character(design.getCharacter());
        }

        return builder.build();
    }
}
