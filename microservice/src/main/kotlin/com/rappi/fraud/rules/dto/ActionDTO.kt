package com.rappi.fraud.rules.dto

import com.rappi.fraud.rules.entities.Action

data class ActionDTO(
    val name: String,
    val params: Map<String, String> = emptyMap()
)

class ActionDTOMapper {
    fun map(action: Action): ActionDTO {
        return ActionDTO(action.name, action.params)
    }
}
