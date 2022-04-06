package com.aldera.multitasker.ui.util

import com.aldera.multitasker.data.models.MultitaskerImage
import com.aldera.multitasker.data.models.UserResponse
import com.aldera.multitasker.data.models.UserTaskResponse

object ConstantTestExecutor {
    val executorList = listOf(
        UserTaskResponse(
            isCompleted = false,
            id = "123123123",
            title = "Ssss",
            performer = UserResponse(
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
            color = "sad",
            importance = 1
        ),
        UserTaskResponse(
            isCompleted = false,
            id = "0009900",
            title = "Sss990s",
            performer = UserResponse(
                id = "3232390090",
                createdAt = "3232332988888",
                email = "testx2@testee.ee",
                name = "Name3",
                avatar = MultitaskerImage(
                    id = "db88fe31-4c61-40f8-8060-af30ad3cc471",
                    path = "/media/2022-04-01 06:25:05.461988_image:27.png",
                    name = "image:27.png",
                    type = "png"
                )
            ),
            color = "sad213",
            importance = 2
        )
    )
}
