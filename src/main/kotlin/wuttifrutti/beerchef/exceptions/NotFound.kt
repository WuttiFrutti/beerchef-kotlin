package wuttifrutti.beerchef.exceptions

class NotFound(what: String, type: ExceptionTypes = ExceptionTypes.NOT_FOUND): Exception("$what has not been found.")
