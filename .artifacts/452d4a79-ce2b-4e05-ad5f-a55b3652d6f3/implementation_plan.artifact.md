# Refactor RoadbookSection to Stateful/Stateless Pattern

This plan refactors the `RoadbookSection` composable in the `:roadbook` module to follow the stateful/stateless pattern. This improves module encapsulation and testability.

## Proposed Changes

### [roadbook]

#### [MODIFY] [RoadbookSection.kt](file:///home/nacho/Desarrollo/src/RoadbookNavigator/roadbook/src/main/java/org/giste/roadbooknavigator/features/roadbook/ui/RoadbookSection.kt)
- Rename current `RoadbookSection` to `RoadbookContent`.
- Create a new stateful `RoadbookSection` that:
    - Injects `RoadbookViewModel` using `hiltViewModel()`.
    - Collects `roadbookState` with lifecycle.
    - Passes the state and ViewModel callbacks to `RoadbookContent`.
- Update Previews to use `RoadbookContent`.

#### [MODIFY] [RoadbookUiTest.kt](file:///home/nacho/Desarrollo/src/RoadbookNavigator/roadbook/src/androidTest/java/org/giste/roadbooknavigator/features/roadbook/ui/RoadbookUiTest.kt)
- Update all test cases to call `RoadbookContent` instead of `RoadbookSection`. This preserves existing UI logic testing in a stateless manner.

### [app]

#### [MODIFY] [DashboardScreen.kt](file:///home/nacho/Desarrollo/src/RoadbookNavigator/app/src/main/java/org/giste/roadbooknavigator/ui/dashboard/DashboardScreen.kt)
- Update the `roadbookSlot` implementation to use the new stateful `RoadbookSection`.
- Remove redundant ViewModel observation and callback piping for the roadbook state.

## Verification Plan

### Automated Tests
- Run `:roadbook:connectedAndroidTest` to ensure UI logic still works correctly with `RoadbookContent`.
- Build the `:app` module to ensure integration is correct.

### Manual Verification
- Deploy the app and verify the Roadbook still loads, scrolls, and allows importing files.
