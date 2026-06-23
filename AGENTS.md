# Agent Rules for Roadbook Navigator

## 1. General Principles
- **Language**: Use English for all code, comments, and documentation.
- **Architecture**: Clean Architecture (Data, Domain, UI layers).
- **Design**: Domain-Driven Design (Entities, Value Objects, Use Cases).
- **Standards**: Follow official Android and Kotlin best practices.

## 2. Package Structure
Organize logic by feature, but UI by screen to ensure synchronization:
- `features.<name>.domain`: Business logic, Entities, Value Objects, Use Cases, Repository interfaces.
- `features.<name>.data`: Repository implementations, local/remote data sources, parsers.
- `features.<name>.ui`: Composables specific to the feature.
- `ui.<screen>`: ViewModels and Composables for specific screens (e.g., `dashboard`, `settings`).
- `core`: Common utilities, theme, and shared base classes.

## 3. Git Workflow
- **Branching**: Use `feature/`, `fix/`, or `refactor/` prefixes.
- **Commits**: Small, atomic, and descriptive.
- **Operations**: Agent can manage local Git (branches, checkout, merges, etc.). **Agent will only perform commits when explicitly requested by the user.** **User MUST handle all push operations** via IDE.
- **Process**: One problem/task at a time. Agent can propose commit messages.

## 4. Testing
- **Unit Tests**: Required for Domain logic (Entities, Use Cases).
- **Integration Tests**: For Data and UI layers where logic integration is key.
- **Exceptions**: Do not test framework internals (Hilt, Room, Compose internals).

## 5. Refactoring & Guidance
- **Primary Source**: User will provide existing logic from previous projects.
- **Agent Role**: Guide the refactoring process, suggest package placement, review Clean Architecture compliance, and ensure DDD principles.
- **Intervention**: Agent should primarily review and advise, only proposing new code to demonstrate patterns or when specifically requested.
