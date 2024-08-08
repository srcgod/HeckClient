package net.kyc.client.util;

import org.lwjgl.glfw.GLFW;

/**
 * @author linus
 * @see GLFW
 * @since 1.0
 */
public class KeyboardUtil {
    /**
     * @param keycode
     * @return
     */
    public static String getKeyName(int keycode, int scancode) {
        return switch (keycode) {
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
            case GLFW.GLFW_KEY_SPACE -> "SPACE";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "LCONTROL";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCONTROL";
            case GLFW.GLFW_KEY_LEFT_ALT -> "LALT";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "RALT";
            case GLFW.GLFW_KEY_ENTER -> "ENTER";
            case GLFW.GLFW_KEY_TAB -> "TAB";
            case GLFW.GLFW_KEY_BACKSPACE -> "BACKSPACE";
            case GLFW.GLFW_KEY_DELETE -> "DELETE";
            case GLFW.GLFW_KEY_INSERT -> "INSERT";

            // Mouse Buttons
            case 1000 -> "MOUSE0";
            case 1001 -> "MOUSE1";
            case 1002 -> "MOUSE2";
            case 1003 -> "MOUSE3";
            case 1004 -> "MOUSE4";
            case 1005 -> "MOUSE5";
            case 1006 -> "MOUSE6";
            case 1007 -> "MOUSE7";
            case 1008 -> "MOUSE8";
            case 1009 -> "MOUSE9";
            default -> GLFW.glfwGetKeyName(keycode, scancode);
        };
    }

    public static String getKeyName(int keycode) {
        return getKeyName(keycode, GLFW.glfwGetKeyScancode(keycode));
    }

    /**
     * @param key
     * @return
     */
    public static int getKeyCode(String key) {
        if (key.equalsIgnoreCase("NONE")) {
            return GLFW.GLFW_KEY_UNKNOWN;
        }
        // Keyboard Keys
        for (int i = 32; i < 97; i++) {
            if (key.equalsIgnoreCase(getKeyName(i,
                    GLFW.glfwGetKeyScancode(i)))) {
                return i;
            }
        }
        for (int i = 256; i < 349; i++) {
            if (key.equalsIgnoreCase(getKeyName(i,
                    GLFW.glfwGetKeyScancode(i)))) {
                return i;
            }
        }
        // Mouse Buttons
        for (int i = 1000; i < 1010; i++) {
            if (key.equalsIgnoreCase(getKeyName(i,
                    GLFW.glfwGetKeyScancode(i)))) {
                return i;
            }
        }
        return GLFW.GLFW_KEY_UNKNOWN;
    }
}
