# RuleFlow Language Reference

## Introduction

The RuleFlow Language is a domain-specific language (DSL) designed to define and evaluate business rules and workflows in a readable, maintainable format. This reference documents the complete language syntax and functionality as implemented in the RuleFlow system.

---

## Workflow Structure

A RuleFlow file is organized into **workflows**, each containing one or more **rulesets**. Each ruleset can have an optional condition and contains one or more **rules**. Rules are evaluated in order, and actions or return values are triggered when a rule matches.

### Basic Structure

```text
workflow 'workflow_name'
    [configuration]
    ruleset 'ruleset_name' [condition] [then]
        'rule_name' (expression [then action(...)] | expression return result [with actions])
        'rule_name2' expression return result
    ruleset 'another_ruleset'
        'rule_name3' expression return result
    default [return] result [with actions]
end
```

### Complete Example

```text
workflow 'fraud_detection'
    evaluation_mode multi_match
    ruleset 'high_risk_checks' user.risk_score > 80 then
        'excessive_velocity' user.transactions_today > 10 return block with manual_review({'priority': 'high'})
        'suspicious_location' distance(user.lat, user.lon, last_login.lat, last_login.lon) > 1000 return block
    ruleset 'standard_checks'
        'basic_validation' user.email_verified = true and user.phone_verified = true return allow
    default return block with action('default_review')
end
```

---

## Configuration Options

### Evaluation Modes

Control how rules are evaluated within a workflow:

- `evaluation_mode single_match` - Stop after first matching rule (default)
- `evaluation_mode multi_match` - Continue evaluating all rules

```text
workflow 'test'
    evaluation_mode multi_match
    ruleset 'checks'
        'rule1' condition1 return result1
        'rule2' condition2 return result2
    default allow
end
```

---

## Ruleset Conditions

Rulesets can have optional conditions that determine whether the ruleset should be evaluated:

```text
ruleset 'premium_user_rules' user.is_premium = true then
    'rule1' amount > 1000 return block
    'rule2' velocity > 5 return review

ruleset 'standard_rules' user.is_premium <> true then
    'rule1' amount > 100 return review

ruleset 'always_evaluated'
    'rule1' suspicious_activity = true return block
```

---

## Rule Syntax

Rules support two main syntaxes:

### 1. Return Syntax (Most Common)
```text
'rule_name' expression return result [with actions]
```

### 2. Then Syntax (Action-First)
```text
'rule_name' (expression then action(...))
```

### Parentheses
Parentheses around the entire rule body are optional:
```text
'rule1' (expression return result)
'rule2' expression return result
```

---

## Expressions

### Boolean Logic
```text
'logic_example' x and y or z return block
'precedence' (x or y) and z return block
'negation' not suspicious return allow
```

**Operator Precedence:** `NOT` → `AND` → `OR`

### Comparisons
| Operator | Description | Example |
|----------|-------------|---------|
| `=` or `==` | Equality | `amount = 100` |
| `<>` | Not equal | `status <> 'pending'` |
| `<`, `<=`, `>`, `>=` | Numeric/string comparison | `age >= 18` |

### Mathematical Operations
```text
'math_example' (amount + fee) * tax_rate > 1000 return block
'modulo' order_id % 10 = 0 return sample
'precedence' x + y * z = 15 return block  # multiplication first
'parentheses' (x + y) * z = 80 return block
```

**Operator Precedence:** `*`, `/`, `%` → `+`, `-`

### List Operations

#### Basic List Membership
```text
'in_list' user_type in 'premium', 'gold', 'platinum' return priority
'contains' allowed_countries contains country_code return allow
'starts_with' email starts_with 'admin', 'support' return escalate
```

#### List Quantifiers on Collections
```text
'any_match' order.items.any { type = 'restricted' } return block
'all_match' order.items.all { verified = true } return allow
'none_match' user.devices.none { flagged = true } return allow
```

#### Aggregation Functions
```text
'count' order.items.count() > 5 return review
'average' order.items.average { price } > 100 return premium
'distinct' order.items.distinct { vendor_id }.count() > 3 return multi_vendor
```

#### Stored Lists
```text
'stored_list' user_id in list('blocked_users') return block
```

#### Property Tuples
```text
'tuple_check' (user.country, user.state) in ('US', 'CA'), ('US', 'NY') return domestic
```

---

## Data Types and Literals

### String Literals
```text
'single_quotes' name = 'John Doe' return match
'special_chars' code = 'ABC-123_XYZ' return valid
```

### Numeric Literals
```text
'integer' count = 42 return valid
'decimal' rate = 3.14159 return calculated
'negative' balance = -100.50 return overdraft
'scientific' threshold = 1.5e10 return high
```

### Boolean Literals
```text
'boolean_true' active = true return enabled
'boolean_false' deleted = false return available
```

### Null Values
```text
'null_check' middle_name = null return no_middle_name
```

---

## Date and DateTime Functions

### Date/DateTime Parsing
```text
'date_literal' order_date = date('2024-06-01') return valid
'datetime_literal' timestamp = datetime('2024-06-01T12:30Z') return valid
'datetime_offset' created = datetime('2024-06-01T12:30+02:00') return valid
'date_property' date(order_date) = date('2024-06-01') return match
```

### Current Time
```text
'now_check' now() > date('2024-01-01') return current_year
'date_now' date(now()) = date('2024-06-15') return today
```

### Date Arithmetic
```text
'add_days' date_add(order_date, 5, day) = date('2024-06-06') return future
'add_hours' date_add(timestamp, 2, hour) = datetime('2024-06-01T14:30Z') return later
'add_minutes' date_add(time, 30, minute) = datetime('2024-06-01T13:00Z') return soon

'subtract_days' date_subtract(expiry_date, 7, day) = date('2024-06-01') return week_before
'subtract_hours' date_subtract(deadline, 1, hour) = datetime('2024-06-01T11:30Z') return hour_before
```

**Supported Time Units:** `day`, `hour`, `minute`

### Date Calculations
```text
'date_diff' date_diff(start_date, end_date, day) = 30 return month_duration
'date_diff_hours' date_diff(login_time, logout_time, hour) > 8 return long_session
'zero_diff' dateDiff(date1, date1, day) = 0 return same_day
```

### Day of Week
```text
'weekend' day_of_week(order_date) in 'SATURDAY', 'SUNDAY' return weekend_order
'monday' day_of_week('2024-06-01') = 'SATURDAY' return specific_day
```

**Day Values:** `MONDAY`, `TUESDAY`, `WEDNESDAY`, `THURSDAY`, `FRIDAY`, `SATURDAY`, `SUNDAY`

---

## String Functions

### Regular Expressions
```text
'strip_prefix' regex_strip(phone, '^\\+1') = '5551234567' return us_number
'extract_user' regex_strip(email, '@.*') = 'username' return user_part
'clean_number' regex_strip(card_number, '^(0|1+0+)+') = '4111111111111111' return cleaned
```

### String Similarity Functions

#### Basic String Distance (Levenshtein-based)
```text
'similar_names' string_distance(input_name, 'John Smith') > 70 return likely_match
'exact_match' string_distance(code1, code2) = 100 return identical
```

#### Fuzzy String Matching
```text
'partial_match' partial_ratio(address1, address2) > 80 return similar_address
'token_sort' token_sort_ratio(name1, name2) > 85 return name_match
'token_set' token_set_ratio(description1, description2) > 75 return similar_desc
'similarity_score' string_similarity_score(text1, text2) > 90 return high_similarity
```

**Note:** All similarity functions return values from 0-100, where 100 is identical.

---

## Geospatial Functions

### Geohash Operations
```text
'encode_location' geohash_encode(lat, lon, 8) = '9q8yyk8y' return sf_area
'encode_default' geohash_encode(37.7749, -122.4194) = 'encoded_location' return valid
'decode_hash' geohash_decode('9q8yyk8y') = coordinates return decoded
```

### Distance Calculations
```text
'coord_distance' distance(lat1, lon1, lat2, lon2) < 100 return nearby
'geohash_distance' distance(geohash1, geohash2) > 1000 return far_apart
'sf_to_la' distance(37.7749, -122.4194, 34.0522, -118.2437) < 600 return california
```

### Proximity Checks
```text
'within_radius' within_radius(user_lat, user_lon, store_lat, store_lon, 50) return local_delivery
'nearby_check' within_radius(37.7749, -122.4194, 34.0522, -118.2437, 600) return west_coast
```

**Note:** All distance values are in kilometers.

---

## Advanced Functions

### Mathematical Functions
```text
'absolute_value' abs(balance) > 1000 return high_magnitude
'absolute_diff' abs(actual - expected) < 0.01 return within_tolerance
```

---

## Actions

Actions are triggered when rules match and can be chained together.

### Action Syntax Variations

#### Explicit Action Function
```text
'rule1' condition return result with action('manual_review')
'rule2' condition return result with action('escalate', {'priority': 'high', 'reason': 'fraud'})
```

#### Shorthand Action Syntax
```text
'rule3' condition return result with manual_review
'rule4' condition return result with escalate({'priority': 'high'})
```

#### Multiple Actions
```text
'multi_action' condition return block with manual_review({'priority': 'high'}) and logout_user
'chain_actions' condition return block with action('review') and action('notify', {'email': 'admin@company.com'})
```

### Action Parameters
```text
'with_params' suspicious = true return block with manual_review({
    'priority': 'high',
    'reason': 'suspicious_activity',
    'reviewer': 'fraud_team'
}) and send_alert({'channel': 'slack'})
```

### Actions in Default Clause
```text
default return allow with action('log_decision', {'rule': 'default'})
```

---

## Property Access

### Simple Properties
```text
'simple' user_id = 123 return match
'dot_notation' .global_config = 'enabled' return configured
```

### Nested Properties
```text
'nested' user.profile.email = 'admin@company.com' return admin
'deep_nested' order.payment.card.type = 'visa' return visa_card
'array_access' user.addresses.primary.country = 'US' return domestic
```

---

## Error Handling and Warnings

The RuleFlow engine provides robust error handling:

### Missing Fields
If a referenced field doesn't exist in the input data:
- A warning is generated: `"field_name field cannot be found"`
- The rule evaluation stops and the default result is returned

### Division by Zero
```text
'safe_division' amount / quantity > 100 return expensive
# If quantity = 0, generates warning and returns default
```

### Invalid Date Formats
```text
'date_validation' date(invalid_date) = date('2024-01-01') return match
# Invalid date strings generate warnings and return default
```

### Type Mismatches
The engine handles type coercion gracefully but may generate warnings for unexpected type operations.

---

## Complete Examples

### Fraud Detection Workflow
```text
workflow 'fraud_detection'
    evaluation_mode multi_match
    
    ruleset 'premium_user_checks' order.custom.user_is_premium = true then
        'premium_velocity' features.mk_payer_approved_qty_90d >= features.mk_payer_approved_qty_60d + 6
            and features.mk_payer_approved_amount_90d >= features.mk_payer_approved_amount_30d + 100
            and features.user_compensations_amount_90d / features.mk_payer_approved_amount_90d < 0.05
            return allow
            
        'premium_anomaly' features.is_card_bin_in_anomaly_detector = true 
            or features.fake_users_user_email_score > 1 
            return block with manual_review({'type': 'premium_user_anomaly'})
    
    ruleset 'geo_checks'
        'location_velocity' distance(user.current_lat, user.current_lon, user.last_lat, user.last_lon) > 1000
            and date_diff(user.last_login, now(), hour) < 2
            return block with action('geo_impossible', {'distance': 'distance_value'})
            
        'restricted_location' within_radius(user.lat, user.lon, restricted_zone.lat, restricted_zone.lon, 10)
            return block with escalate({'reason': 'restricted_area'})
    
    ruleset 'pattern_matching'
        'similar_merchant' string_distance(merchant.name, known_fraudster.name) > 85
            return review with flag_similarity({'match_score': 'similarity_value'})
            
        'suspicious_email' regex_strip(user.email, '@.*') in list('known_fraud_patterns')
            return block with immediate_review
    
    default return allow with log_decision({'workflow': 'fraud_detection'})
end
```

### E-commerce Risk Assessment
```text
workflow 'ecommerce_risk'
    ruleset 'order_validation'
        'high_value' order.total > 5000 and user.account_age_days < 30
            return manual_review with escalate({'amount': order.total})
            
        'bulk_order' order.items.count() > 20 
            or order.items.any { quantity > 100 }
            return review with bulk_order_check
            
        'international' user.country <> order.shipping.country
            and distance(user.country_lat, user.country_lon, order.shipping.lat, order.shipping.lon) > 2000
            return verify_shipping with international_alert
    
    ruleset 'user_behavior'
        'velocity_check' user.orders_today > 5 
            and user.orders.all { status = 'completed' }
            return velocity_limit with rate_limit_user
            
        'device_fingerprint' user.devices.distinct { fingerprint }.count() > 10
            and date_diff(user.first_login, now(), day) < 7
            return suspicious_device with device_review
    
    default return approve
end
```

---

## Best Practices

### Expression Clarity
- Use parentheses to clarify precedence in complex expressions
- Break long conditions across multiple lines for readability
- Use meaningful rule names that describe the business logic

### Error Handling
- Always provide a default clause
- Consider field existence when referencing nested properties
- Test edge cases like division by zero and invalid dates

### Performance Considerations
- Place most selective conditions first in AND expressions
- Use `single_match` evaluation mode when appropriate
- Consider using stored lists for frequently referenced data

### Action Design
- Use descriptive action names and parameter keys
- Include relevant context in action parameters
- Chain related actions together logically

---

## Language Keywords

### Reserved Keywords
`workflow`, `ruleset`, `default`, `end`, `return`, `with`, `and`, `or`, `not`, `then`, `in`, `contains`, `starts_with`, `any`, `all`, `none`, `count`, `average`, `distinct`, `list`, `action`, `true`, `false`, `null`

### Case Sensitivity
- Keywords are case-insensitive: `WORKFLOW`, `workflow`, `Workflow` all work
- Property names and string literals are case-sensitive
- Function names are case-insensitive with multiple variations supported

---

## Grammar Notes

- Comments: Single-line `--` and multi-line `/* */`
- String literals: Single quotes `'text'` (double quotes in grammar but single quotes in practice)
- Identifiers: Letters, numbers, underscores; must start with letter or underscore
- Whitespace: Spaces, tabs, newlines are ignored outside of string literals

---

*This documentation reflects the complete implemented functionality as of the current version. For additional examples and edge cases, refer to the test suite.*
