package wuttifrutti.beerchef.exceptions

class DidNotWrite(what: String, type: ExceptionTypes = ExceptionTypes.DID_NOT_WRITE): Exception("$what was not saved.")

