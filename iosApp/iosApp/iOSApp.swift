import SwiftUI
import GoogleMaps
import GooglePlaces


@main
struct iOSApp: App {

    init() {
        let filePath = Bundle.main.path(forResource: "AppSecrets", ofType: "plist")!
            let plist = NSDictionary(contentsOfFile: filePath)!
            let googleMapsApiKey = plist["GOOGLE_MAPS_API_KEY"] as! String


        GMSServices.provideAPIKey(googleMapsApiKey)
        GMSPlacesClient.provideAPIKey(googleMapsApiKey)
    }


    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
    
    
}
