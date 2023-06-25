//
//  OutagesScreen.swift
//  iosApp
//
//  Created by Joel Kanyi on 25/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct OutagesScreen: View {
    @StateObject var outagesViewModel = OutagesViewModel()
    var body: some View {
        NavigationView {
            ZStack {
                VStack(alignment: .leading) {
                    Text("Power Interruptions")
                            .font(.title2)
                            .frame(maxWidth: .infinity, alignment: .top)

                    if outagesViewModel.outageInformationUiState.isLoading {
                        ProgressView("Getting Outages...")
                                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                    } else if outagesViewModel.outageInformationUiState.message != nil {
                        Text(outagesViewModel.outageInformationUiState.message!)
                                .font(.subheadline)
                                .foregroundColor(.red)
                                .frame(maxWidth: .infinity, alignment: .center)
                    } else {
                        List {
                            ForEach(outagesViewModel.outageInformationUiState.outages, id: \.self) { outage in
                                OutageCard(outage: outage, onOutageClicked: { outage in
                                    print("outage: \(outage.region)")
                                })
                            }
                        }
                                .listStyle(PlainListStyle())

                    }
                }
            }
        }
                .navigationBarTitle("Power Interruptions")
                .onAppear {
                    outagesViewModel.getOutages()
                    outagesViewModel.startObserving()
                }
                .onDisappear {
                    outagesViewModel.dispose()
                }
    }
}

struct OutageCard: View {
    let outage: OutageInformationUiState
    let onOutageClicked: (OutageInformationUiState) -> Void
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                // Color(0XFF0B4A7A)
                Text(outage.region)
                        .font(.subheadline)
                        .foregroundColor(.primary)
                Spacer()
                Text(outage.area ?? "")
                        .font(.subheadline)
            }

            Spacer()
                    .frame(height: 12)

            HStack {
                Text(outage.date)
                        .font(.subheadline)
                Spacer()
                Text("\(outage.startTime) - \(outage.endTime)")
                        .font(.subheadline)
            }

            Spacer()
                    .frame(height: 12)

            Text(
                    outage.places
                            .map {
                                $0
                            }
                            .joined(separator: ", ")
            )
                    .font(.system(size: 12))
                    .foregroundColor(.primary)
                    .italic()
        }
                .padding()
                .onTapGesture {
                    onOutageClicked(outage)
                }
    }
}
