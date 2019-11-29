package com.rappi.fraud.rules.entities.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {

    override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}

class LocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {

    override fun deserialize(jsonParser: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
        return LocalDateTime.parse(jsonParser?.readValueAs(String::class.java))
    }
}
