package org.example;

import java.util.HashMap;

public class ActionTypeMapper {
    public static final HashMap<String, ActionType> map = new HashMap<>() {{
        put("Hit", ActionType.HIT);
        put("Stand", ActionType.STAND);
        put("Joined", ActionType.JOIN);
        put("Left", ActionType.LEAVE);
        put("Redeal", ActionType.REDEAL);
        put("Win", ActionType.WIN);
        put("Show", ActionType.SHOW);
    }};
}
