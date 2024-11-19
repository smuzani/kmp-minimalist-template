import SwiftUI
import shared

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