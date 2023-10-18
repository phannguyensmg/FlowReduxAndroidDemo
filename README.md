# What is Redux

**Redux** is a predictable state management pattern used in software development to manage and control the state (data) of an application. It provides a structured, centralized, and unidirectional data flow approach that promotes maintainability, predictability, and ease of debugging

## Key components of the Redux concept include:
1.  **Store**: A central repository that holds the entire application's state. This state can represent data, settings, and other information relevant to the application.

2.  **Actions**: Descriptions of events or user interactions that trigger state changes. Actions are typically plain objects containing a type and, optionally, additional data (payload) related to the action.

3.  **Reducers**: Pure functions that define how the application's state changes in response to actions. Reducers take the current state and an action as input and produce a new state as output.

4. **Side effect**:   Refers to any behavior or operation that occurs as a result of, or in response to, an action being dispatched

5. **State** : Refers to the data that represents the entire state of your application at a given point in time. This state data includes all the information and variables necessary to describe the current condition of your application

## What is benefits of Redux:

1. **Predictable State Management**: Redux enforces a strict and predictable state management pattern. The state of your application is stored in a single store, and updates are made by dispatching actions, which describe the changes to the state.

2. **Single Source of Truth**: With Redux, there is a single source of truth for the state of your application. This makes it easier to understand and debug the state of your application, as well as to maintain consistency across your application.

3. **State Immutability**: Redux encourages immutable state. Instead of directly modifying the state, you create a new state object for each update. This makes it easier to track changes and maintain a history of state changes.

4. **Time-Travel Debugging**: Redux supports time-travel debugging, which allows you to step forward and backward through the state changes in your application. This can be incredibly useful for tracking down bugs and understanding how your application reached a particular state.

5. **Testability**: Redux applications are generally more testable because the state changes are isolated and predictable. You can easily write unit tests for reducers and actions.

6. **Separation of Concerns**: Redux promotes a clear separation of concerns by keeping the state logic in reducers and actions separate from the UI components. This makes your codebase more maintainable and scalable.

7. **Scaling and Performance**: Redux is designed to handle complex state management efficiently. With proper usage, it can help you maintain good performance in your application even as it grows.

### You can find more info here [RxRedux](https://github.com/freeletics/RxRedux). This is redux implementation based on RxJava. And FlowRedux is redux implementation base on coroutine flow, it has the same components, characteristics as RxRedux

# More info about this demo
This demo shows how to apply FlowRedux in the Homegate interview assessment test. You will find 2 features: home (result list) and detail in the app module. Each feature has its own state machine where hold the logic of the feature. I also applied CLEAN for data and domain layer.

In the **main** branch, i implemented the basic redux with **FlowReduxStateMachine** contains: reducer, side effects. I also defined actions, state used in the state machine for each feature.

In the **composite-redux** branch, i implemented a composite state machine where contains sub state machine. In this demo, it displays similar properties in the result list (home screen). The logic of similar properties component is separated into sub state machine and plug into the composite state machine. In this way, it helps us separate component's logic and easier to maintain.
