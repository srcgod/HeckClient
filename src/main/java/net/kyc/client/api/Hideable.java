package net.kyc.client.api;

public interface Hideable {
    /**
     * @return
     */
    boolean isHidden();

    /**
     * @param hidden
     */
    void setHidden(boolean hidden);
}
