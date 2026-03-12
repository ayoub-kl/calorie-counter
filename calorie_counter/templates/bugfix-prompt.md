# Bugfix prompt

Use this in Cursor Agent:

Investigate and fix this bug in the repository.

BUG:
[describe bug]

EXPECTATIONS:
- inspect relevant files before changing code
- identify root cause, not only symptom
- keep the fix minimal and correct
- preserve architecture boundaries
- do not introduce speculative refactors
- add/update tests if the bug involves domain logic, mapping, or state transitions

OUTPUT:
- fixed implementation
- concise explanation of root cause
- any assumptions or limitations
