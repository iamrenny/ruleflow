package com.rappi.fraud.rules.entities

import com.rappi.fraud.rules.parser.RuleEngine
import io.reactiverse.reactivex.pgclient.Row
import java.time.Duration

data class ActiveWorkflow(
    val countryCode: String,
    val name: String,
    val workflowId: Long,
    @Transient
    val workflow: String? = null,
    @Transient
    val engine: RuleEngine? = null
) : Cacheable(Duration.ofMinutes(5)) {

    constructor(row: Row) : this(
            countryCode = row.getString("country_code"),
            name = row.getString("name"),
            workflowId = row.getLong("workflow_id"),
            workflow = row.getString("workflow")
    )
}

data class ActiveKey(
    val countryCode: String,
    val name: String
)
