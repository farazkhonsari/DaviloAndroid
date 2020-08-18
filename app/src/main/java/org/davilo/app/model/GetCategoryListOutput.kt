package org.davilo.app.model

data class GetCategoryListOutput(var module: m)

data class m(val categories: ArrayList<Category>)
