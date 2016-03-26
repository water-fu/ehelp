package com.wisdom.common.encrypt;

public class MD5Encrypt implements IEncrypt {

    public String encodePassword(String rawPass, Object salt) {
        String afterPass = salt.toString()+rawPass;
        return MD5.md5(afterPass);
    }

    public boolean isPasswordValid(String encPass, String rawPass, Object salt){
        String afterPass = this.encodePassword(rawPass, salt);
        return encPass.equalsIgnoreCase(afterPass);
    }

    @Deprecated
    @Override
    public String decodePassword(String rawPass, Object salt) {
        return null;
    }
}
