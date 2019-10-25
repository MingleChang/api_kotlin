package mingle.chang.api.models

class Response {
    val result: Any?
    val code: Int
    val message: String
    constructor() {
        this.result = null
        this.code = 200
        this.message = "success"
    }
    constructor(result: Any?) {
        this.result = result
        this.code = 200
        this.message = "success"
    }
    constructor (code: Int = 200, message: String = "success"){
        this.result = null
        this.code = code
        this.message = message
    }
    constructor(result: Any?, code: Int = 200, message: String = "success") {
        this.result = result
        this.code = code
        this.message = message
    }
}