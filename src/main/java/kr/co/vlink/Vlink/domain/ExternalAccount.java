package kr.co.vlink.Vlink.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExternalAccount {
    private String domain;
    private String account;

    @Builder
    public ExternalAccount(String domain, String account) {
        this.domain = domain;
        this.account = account;
    }
}
