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
    @State private var searchText = ""
    var body: some View {
        NavigationView {
            VStack(alignment: .leading) {
                if outagesViewModel.outageInformationUiState.isLoading {
                    ProgressView("Getting Outages...")
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                } else if outagesViewModel.outageInformationUiState.message != nil {
                    Text(outagesViewModel.outageInformationUiState.message!)
                            .font(.subheadline)
                            .foregroundColor(.red)
                            .frame(maxWidth: .infinity, alignment: .center)
                } else if (outagesViewModel.outageInformationUiState.outages.isEmpty && !outagesViewModel.outageInformationUiState.isLoading) {
                    Text("No outages found")
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
                                .listRowSeparator(.hidden)
                    }
                            .listStyle(.plain)
                }
            }
                    .navigationBarTitleDisplayMode(.inline)
                    .toolbar {
                        ToolbarItem(placement: .principal) {
                            Text("Power Interruptions")
                                    .font(.title2)
                        }
                    }
                    .searchable(text: $searchText)
                    .onChange(of: searchText) { searchText in
                        outagesViewModel.getOutages(searchString: searchText)
                    }
                    .autocorrectionDisabled()
        }
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
        HStack {
            Divider()
                    .frame(width: 4)
                    .background(Color("BlueColor"))
            VStack {
                Spacer()
                        .frame(height: 12)
                HStack {
                    Text(outage.region)
                            .font(.caption)
                            .foregroundColor(Color("BlueColor"))
                            .fontWeight(.bold)
                    Spacer()
                    Text(outage.area ?? "")
                            .font(.caption)
                            .foregroundColor(.primary)
                            .fontWeight(.semibold)
                }
                        .padding(.trailing, 8)

                Spacer()
                        .frame(height: 12)

                HStack {
                    Text(outage.date)
                            .font(.caption)
                    Spacer()
                    Text("\(outage.startTime) - \(outage.endTime)")
                            .font(.caption)
                }
                        .padding(.trailing, 8)

                Spacer()
                        .frame(height: 12)

                Text(
                        outage.places
                                .map {
                                    $0
                                }
                                .joined(separator: ", ")
                )
                        .font(.caption)
                        .fontWeight(.light)
                        .padding(.trailing, 8)
                        .frame(maxWidth: .infinity)
                Spacer()
                        .frame(height: 12)
            }
        }
                .background(Rectangle().fill(Color.white))
                .cornerRadius(6)
                .shadow(color: .gray, radius: 0.5)
                .onTapGesture {
                    onOutageClicked(outage)
                }
    }
}
