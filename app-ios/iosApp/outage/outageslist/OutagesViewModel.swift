//
//  OutagesViewModel.swift
//  iosApp
//
//  Created by Joel Kanyi on 25/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension OutagesScreen {
    @MainActor class OutagesViewModel: ObservableObject {
        @LazyKoin
        var viewModel: OutageViewModel

        private var handle: DisposableHandle?

        @Published var outageInformationUiState: OutageInformation = OutageInformation(
                message: nil,
                isLoading: false,
                outages: []
        )

        func getOutages() {
            viewModel.fetchOutages()
        }

        func startObserving() {
            handle = viewModel.outageInformationUiState.subscribe(onCollect: { state in
                if let state = state {
                    self.outageInformationUiState = state
                }
            })
        }

        func dispose() {
            handle?.dispose()
        }
    }
}
