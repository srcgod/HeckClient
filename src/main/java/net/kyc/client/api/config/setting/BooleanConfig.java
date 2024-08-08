package net.kyc.client.api.config.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyc.client.api.config.Config;

import java.util.function.Supplier;

/**
 * @author linus
 * @since 1.0
 */
public class BooleanConfig extends Config<Boolean> {
    public BooleanConfig(String name, String desc, Boolean val) {
        super(name, desc, val);
    }

    public BooleanConfig(String name, String desc, Boolean val,
                         Supplier<Boolean> visible) {
        super(name, desc, val, visible);
        configAnimation.setStateHard(val);
    }

    @Override
    public void setValue(Boolean in) {
        super.setValue(in);
        configAnimation.setState(in);
    }

    /**
     * @return
     */
    @Override
    public JsonObject toJson() {
        JsonObject configObj = super.toJson();
        configObj.addProperty("value", (Boolean) getValue());
        return configObj;
    }

    /**
     * @param jsonObj The data as a json object
     */
    @Override
    public Boolean fromJson(JsonObject jsonObj) {
        if (jsonObj.has("value")) {
            JsonElement element = jsonObj.get("value");
            return element.getAsBoolean();
        }
        return null;
    }
}
