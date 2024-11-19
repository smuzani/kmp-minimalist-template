import SwiftUI
import shared

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