package me.augusto.composebookapp.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class DescriptionDeserializer : JsonDeserializer<String> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String? {
        if (json == null || json.isJsonNull) {
            return null
        }

        return try {
            if (json.isJsonObject) {
                val jsonObject = json.asJsonObject
                if (jsonObject.has("value")) {
                    val valueElement = jsonObject.get("value")
                    if (valueElement.isJsonPrimitive) {
                        valueElement.asString
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
            else if (json.isJsonPrimitive) {
                json.asString
            }
            else {
                null
            }
        } catch (e: Exception) {
            println("Error parsing description: ${e.message}")
            null
        }
    }
}