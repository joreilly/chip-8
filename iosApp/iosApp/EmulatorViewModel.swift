import Foundation
import Shared

@MainActor
class EmulatorViewModel: ObservableObject {
    @Published var screenData = [Bool](repeating: false, count: 2048)
    
    private let emulator: Emulator
    init(emulator: Emulator) {
        self.emulator = emulator
        
        if let data = loadFile() {
            self.emulator.loadRom(romData: convertNSDataToByteArray(data: data as NSData))
            
            Task {
                for try await screen in self.emulator.screen {
                    let screenData = (screen as! Screen).screenData
                    if (screenData.count > 0) {
                        self.screenData = screenData
                    }
                }
            }
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
