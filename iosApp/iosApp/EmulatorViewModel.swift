import Foundation
import Shared
import KotlinStdlib

class EmulatorViewModel: ObservableObject {
    @Published var screenData = [Bool](repeating: false, count: 2048)
    
    private let emulator: Emulator
    init(emulator: Emulator) {
        self.emulator = emulator
        
        if let data = loadFile() {
            self.emulator.loadRom(romData: convertNSDataToByteArray(data: data as NSData))

            self.emulator.observeScreenUpdates(success: { screenData in
                self.screenData = screenData
            })
        }
    }
    
    func keyPressed(key: Int32) {
        emulator.keyPressed(key: key)
    }

    func keyReleased() {
        emulator.keyReleased()
    }

    func loadFile() -> Data? {
        guard let fileURL = Bundle.main.url(forResource: "Space Invaders [David Winter]", withExtension: "ch8") else {
            print("Failed to create URL for file.")
            return nil
        }
        do {
            let data = try Data(contentsOf: fileURL)
            return data
        }
        catch {
            print("Error opening file: \(error)")
            return nil
        }
    }
    
}
