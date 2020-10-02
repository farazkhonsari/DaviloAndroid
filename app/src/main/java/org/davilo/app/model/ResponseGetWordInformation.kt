package org.davilo.app.model

data class ResponseGetWordInformation(
    var word: Word? = null
)

data class Word(
    var text: String? = null,
    var word_info: List<WordInfo>? = null
)

data class WordInfo(
    var word_type: String? = null,
    var pronunciation: String? = null,
    var table: Table? = null,
    var meanings: List<String>? = null,
    var examples: List<String>? = null,
    var synonyms_elements: List<String>? = null,
    var antonyms_elements: List<String>? = null
)

data class Table(
    var rows: List<Row>? = null,
    var tail: Tail? = null
)

data class Row(
    var columns: List<Column>? = null,
    var title: String? = null
)

data class Column(
    var elements: List<String>? = null,
    var titile: String? = null
)

data class Tail(
    var text: String? = null,
    var link: String? = null
)