import SwiftUI
import GoogleMaps
import GooglePlaces


@main
struct iOSApp: App {

    init() {
        GMSServices.provideAPIKey("AIzaSyAYDqJ1OWT4xqvlY1S2j-f_m3x9_OqmrTE")
        GMSPlacesClient.provideAPIKey("AIzaSyAYDqJ1OWT4xqvlY1S2j-f_m3x9_OqmrTE")
    }
    

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
    
    
}
