package com.example.app.domain.exception

abstract class BaseException : RuntimeException {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Throwable) : super(message, cause)
}
