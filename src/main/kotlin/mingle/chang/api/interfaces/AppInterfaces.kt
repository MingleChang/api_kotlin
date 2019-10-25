package mingle.chang.api.interfaces

import mingle.chang.api.entities.App
import org.springframework.data.jpa.repository.JpaRepository

interface AppRespository : JpaRepository<App, String> {

}