package com.mariabeyrak.scatter.models.requests.getaccount;

public class Account {
    private String name;
    private String authority;
    private String publicKey;
    private String blockchain;
    private String chainId;
    private Boolean isHardware;

    public Account(String name, String authority, String publicKey, String blockchain, String chainId, Boolean isHardware) {
        this.name = name;
        this.authority = authority;
        this.publicKey = publicKey;
        this.blockchain = blockchain;
        this.chainId = chainId;
        this.isHardware = isHardware;
    }
}
