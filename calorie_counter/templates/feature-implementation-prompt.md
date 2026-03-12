# Feature implementation prompt

Use this in Cursor Agent:

Implement the following feature in this repository while obeying all project rules:

FEATURE:
[describe feature]

GOAL:
[describe expected user outcome]

REQUIREMENTS:
- follow existing architecture and naming
- inspect nearby files first
- update all impacted layers
- keep business logic out of composables
- add or update tests for non-trivial logic
- do not invent backend fields silently
- preserve correction-first UX if the feature touches AI meal analysis

DELIVERABLE:
- complete code changes
- any required models/mappers/use cases
- brief summary of what changed and any assumptions/TODOs
