import Foundation
import shared

extension UserListScreen {
    @MainActor class IOSRandomUserViewModel: ObservableObject {
        private let viewModel: RandomUserViewModel
        private var handle: DisposableHandle?

        @Published var state: RandomUserState = RandomUserState(
            users: [],
            filteredUsers: [],
            isLoading: false,
            error: nil
        )

        init(getRandomUsers: GetRandomUsers) {
            self.viewModel = RandomUserViewModel(
                getRandomUsers: getRandomUsers,
                coroutineScope: nil
            )
        }

        func onEvent(event: RandomUserEvent) {
            viewModel.onEvent(event: event)
        }

        func startObserving() {
            handle = viewModel.state.subscribe { [weak self] state in
                if let state = state {
                    self?.state = state
                }
            }
        }

        func dispose() {
            handle?.dispose()
        }
    }
}