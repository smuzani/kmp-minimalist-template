import SwiftUI
import shared

struct ContentView: View {
	@StateObject private var viewModel = RandomUserViewModel()
	
	var body: some View {
		NavigationView {
			Group {
				if viewModel.isLoading {
					ProgressView()
						.progressViewStyle(CircularProgressViewStyle())
				} else if let error = viewModel.error {
					ErrorView(message: "Error: \(error)", onRetry: viewModel.loadUsers)
				} else if viewModel.users.isEmpty {
					Text("No users found")
				} else {
					List(viewModel.users, id: \.login.uuid) { user in
						UserCardView(user: user)
					}
				}
			}
			.navigationTitle("Random Users")
		}
	}
}

struct UserCardView: View {
	let user: UserDto
	
	var body: some View {
		HStack(spacing: 16) {
			AsyncImage(url: URL(string: user.picture.medium)) { image in
				image
					.resizable()
					.aspectRatio(contentMode: .fill)
			} placeholder: {
				ProgressView()
			}
			.frame(width: 60, height: 60)
			.clipShape(Circle())
			
			VStack(alignment: .leading, spacing: 4) {
				Text("\(user.name.first) \(user.name.last)")
					.font(.headline)
				Text(user.email)
					.font(.subheadline)
				Text("\(user.location.city), \(user.location.country)")
					.font(.caption)
				Text("Age: \(user.dob.age)")
					.font(.caption)
			}
		}
		.padding(.vertical, 8)
	}
}

struct ErrorView: View {
	let message: String
	let onRetry: () -> Void
	
	var body: some View {
		VStack(spacing: 16) {
			Text(message)
			Button("Retry", action: onRetry)
		}
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}