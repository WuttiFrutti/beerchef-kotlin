package wuttifrutti.beerchef.exceptions

class NotAllowed(what: String, type: ExceptionTypes = ExceptionTypes.NOT_ALLOWED): Exception("$what is not allowed.")
