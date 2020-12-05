package com.example.app.domain.exception

class PersistenceException : BaseException {
  constructor(message: String) : super(message)
  constructor(message: String, throwable: Throwable) : super(message, throwable)
}
