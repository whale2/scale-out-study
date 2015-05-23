class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?/$subid?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
