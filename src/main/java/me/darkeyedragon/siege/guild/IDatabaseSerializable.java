package me.darkeyedragon.siege.guild;

import java.util.HashMap;
import java.util.Map;

public interface IDatabaseSerializable {
    Map<String, Object> serializeObject();
}
