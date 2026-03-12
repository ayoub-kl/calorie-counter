# Refactor prompt

Use this in Cursor Agent:

Refactor the specified area while preserving behavior.

SCOPE:
[describe files/modules]

OBJECTIVE:
[describe why refactor is needed]

RULES:
- preserve current behavior unless explicitly told otherwise
- avoid broad rewrites outside the requested scope
- maintain or improve layer separation
- remove dead code and unused imports
- keep the diff reviewable
- do not invent new contracts or storage assumptions

OUTPUT:
- refactored code
- concise summary of what improved
- note any risks or follow-up items
