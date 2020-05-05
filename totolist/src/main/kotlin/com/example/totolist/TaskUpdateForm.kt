package com.example.totolist

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class TaskUpdateForm {
    var id: Long? = null

    @NotBlank
    @Size(max = 20)
    var content: String? = null

    var done: Boolean = false
}