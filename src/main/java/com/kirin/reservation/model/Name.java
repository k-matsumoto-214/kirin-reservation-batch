package com.kirin.reservation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Name {
  NAO("尚大"),
  KYO("匡平");

  private final String value;
}
