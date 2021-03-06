package com.aldera.multitasker.ui.util

import com.aldera.multitasker.data.models.MultitaskerImage
import com.aldera.multitasker.data.models.Performer
import com.aldera.multitasker.data.models.UserTaskResponse

object ConstantTestUserTask {
    val taskList = listOf(
        UserTaskResponse(
            isCompleted = true,
            id = "12345",
            title = "Title Subtask",
            performer = Performer(
                id = "32323",
                createdAt = "3232332",
                email = "test@testee.ee",
                name = "Name2",
                avatar = MultitaskerImage(
                    id = "db88fe31-4c61-40f8-8060-af30ad3cc471",
                    path = "/media/2022-04-01 06:25:05.461988_image:27.png",
                    name = "image:27.png",
                    type = "png"
                )
            ),
            color = "#A1B815",
            importance = 2
        ),
        UserTaskResponse(
            isCompleted = true,
            id = "67890",
            title = "Title Subtask2",
            performer = Performer(
                id = "32323",
                createdAt = "3232332",
                email = "test@testee.ee",
                name = "Name",
                avatar = MultitaskerImage(
                    id = "db88fe31-4c61-40f8-8060-af30ad3cc471",
                    path = "/media/2022-04-01 06:25:05.461988_image:27.png",
                    name = "image:27.png",
                    type = "png"
                )
            ),
            color = "#F16500",
            importance = 3
        )
    )
}
