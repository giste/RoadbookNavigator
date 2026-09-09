# Agent Rules for Roadbook Navigator

## 1. General Principles
- **Language**: Use English for all code, comments, and documentation.
- **Architecture**: Clean Architecture (Data, Domain, UI layers).
- **Library First**: Every module (except `:app` and `:settings`) must be treated as a potential standalone library. This means they must have zero knowledge of the host application.
- **Standards**: Follow official Android and Kotlin best practices, including the use of `explicitApi()` for stable library modules.

## 2. Package Structure & API Boundary
Modules must follow a strict separation between their **Public API** and **Internal Implementation**:

### Root Package (`org.giste.<module>`) - The Public API
Only the following items are allowed in the root package and must be `public`:
- **Entry Points**: Main Composables (e.g., `MapScreen`) that handle DI/ViewModel wiring.
- **Provider Interfaces**: Logical gateways that group internal logic (e.g., `MapSettingsProvider`).
- **Domain Models**: Data classes used in the API contract (e.g., `MapSettings`).
- **UI Contract**: Module-specific dimensions (e.g., `MapDimensions`).
- **Logger**: A module-specific logging interface (e.g., `MapLogger`).

### Sub-packages - The Internal Implementation
All other code must be marked as `internal` and reside in sub-packages:
- `org.giste.<module>.domain.usecase`: Internal business logic.
- `org.giste.<module>.data`: Internal repository and datasource implementations.
- `org.giste.<module>.ui`: Stateless content composables and internal ViewModels.

## 3. Communication & Decoupling
- **ViewModels**:
    - Internal ViewModels must only depend on internal Use Cases.
    - Host app ViewModels (in `:app` or `:settings`) must only depend on the **Provider Interfaces** exposed in a module's root package.
- **Dependency Inversion**: If a module needs a service (like Location), it must define its own `Provider` interface. The host app is responsible for providing a `Bridge` implementation via Hilt.
- **Namespacing**: To prevent collisions in a library context:
    - **Resources**: All resource IDs must be prefixed with the module name (e.g., `map_title`).
    - **Persistence**: DataStore, Database, and File paths must be unique (e.g., `org.giste.map.settings`).
    - **WorkManager**: Background task tags must be namespaced.

## 4. UI Styling
- **Independence**: Modules must not depend on the host app's theme (e.g., `RoadbookNavigatorTheme`).
- **Contract**: Define a local `Dimensions` data class and provide it via `CompositionLocal`.
- **Adaptability**: Use standard Material 3 slots so the module automatically inherits colors from the host app's `MaterialTheme`.

## 5. Git Workflow
- **Branching**: Use `feature/`, `fix/`, or `refactor/` prefixes.
- **Commits**: Small, atomic, and descriptive.
- **Operations**: Agent can manage local Git (branches, checkout, merges, etc.). **Agent will only perform commits when explicitly requested by the user.** **User MUST handle all push operations** via IDE.
- **Process**: One problem/task at a time. Agent can propose commit messages.

## 6. Testing
- **Unit Tests**: Required for Domain logic (Entities, Use Cases).
- **Integration Tests**: For Data and UI layers where logic integration is key.
- **Exceptions**: Do not test framework internals (Hilt, Room, Compose internals).

## 7. Refactoring & Guidance
- **Primary Source**: User will provide existing logic from previous projects.
- **Agent Role**: Guide the refactoring process, suggest package placement, review Clean Architecture compliance, and ensure DDD principles.
- **Intervention**: Agent should primarily review and advise, only proposing new code to demonstrate patterns or when specifically requested.
