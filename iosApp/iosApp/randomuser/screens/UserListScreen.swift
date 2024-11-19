import SwiftUI
import shared

struct UserListScreen: View {
    @ObservedObject var viewModel: IOSRandomUserViewModel

    init(getRandomUsers: GetRandomUsers) {
        self.viewModel = IOSRandomUserViewModel(getRandomUsers: getRandomUsers)
    }

    var body: some View {
        ZStack {
            if viewModel.state.isLoading {
                ProgressView()
            } else if let error = viewModel.state.error {
                ErrorView(
                    message: "Error loading users: \(error)",
                    onRetry: { viewModel.onEvent(event: RandomUserEvent.RetryLoadUsers()) }
                )
            } else if viewModel.state.users.isEmpty {
                Text("No users found")
            } else {
                List(viewModel.state.users, id: \.login.uuid) { user in
                    UserCardView(user: user)
                }
                .listStyle(.plain)
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}
