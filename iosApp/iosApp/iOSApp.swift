import SwiftUI
import Firebase
import ComposeApp

@main
struct iOSApp: App {

    init() {
        FirebaseApp.configure()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
				.onOpenURL { url in
					DeepLinkManager.shared.onUrlReceived(url: url.absoluteString)
				}
		}
	}
}
