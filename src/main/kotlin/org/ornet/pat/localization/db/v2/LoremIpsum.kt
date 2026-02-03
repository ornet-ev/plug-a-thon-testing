package org.ornet.pat.localization.db.v2

// words based on https://de.lorem-ipsum.info/
// class inspired by de.svenjacobs.loremipsum.LoremIpsum
class LoremIpsum(private val characterSet: CharacterSet = CharacterSet.LATIN) {
    fun getWords(amount: Int = characterSet.size, startIndex: Int = 0): String {
        if (startIndex >= 0 && startIndex < characterSet.size) {
            var word = startIndex
            val lorem = StringBuilder()

            for (i in 0 until amount) {
                if (word == characterSet.size) {
                    word = 0
                }

                lorem.append(characterSet.words[word])
                if (i < amount - 1) {
                    lorem.append(characterSet.wordSeparator)
                }

                ++word
            }

            return lorem.toString()
        }

        throw IndexOutOfBoundsException("startIndex must be >= 0 and < ${characterSet.size}")
    }


    fun getParagraphs(amount: Int = characterSet.size): String {
        val lorem = StringBuilder()
        for (i in 0..<amount) {
            lorem.append(characterSet.text)
            if (i < amount - 1) {
                lorem.append("\n\n")
            }
        }

        return lorem.toString()
    }

    enum class CharacterSet(val wordSeparator: String, val text: String) {
        LATIN(
            " ",
            "Ut sit dico integre legendos, autem mentitum est ei. Quod tantas equidem te his, oratio vocibus imperdiet ne duo. Ut dicam phaedrum dissentias eam. Noluisse pertinax gloriatur an vis."
        ),
        CHINESE(
            "。",
            "地品詳目将扱要段歯潤打意踊強去汐身全真。日注横伴鉱福健鳥力十無覧載夕。不無件年属応訃座保試記賞具写館回。世写五政概勝改極長奈率企礼。例関半要内筆掲櫛取戒商勢治訃適仕。石枚平代海名田売関既園古体下意。混本天大治掲芝議保難県新聞経。化小年第住価光事目効関県紹話弁月日紙。断社恵入転暮涯帯薄負端拍。覧難用入発社茶参新長綴勝話。"
        ),
        CYRILLIC(
            " ",
            "Лорем ипсум долор сит амет, яуи дицта граеци алияуам еи, новум ерудити цонцлусионемяуе ет меа, еум еа иусто аццусамус либерависсе. Либрис яуаеяуе пертинах ин усу, еу нусяуам вертерем перпетуа при. Яуис синт граеци пер не, ин дуис опортере хис, еу сеа еиус бруте проприае. Нец виде аеяуе делецтус еи, при ид омиттам лобортис рецтеяуе. Яуандо импердиет иус еи, ад лорем цонцептам."
        ),
        GREEK(
            " ",
            "Λορεμ ιπσθμ δολορ σιτ αμετ, δετραcτο ινιμιcθσ δθο εα, μεισ νοvθμ cονγθε ιν cθμ, σολθμ μαλορθμ αccθσαμ qθο αδ. Μεα αθδιαμ διγνισσιμ ιντελλεγαμ ιν, ταμqθαμ ανcιλλαε σιγνιφερθμqθε μει ιδ. Cθ φαcιλισ περιcθλα δεφινιτιονεμ μει, νο απεριρι αccθσατα περ, ατqθι ινσολενσ μεα τε. Δεβετ ιθδιcο τηεοπηραστθσ εξ."
        );

        val size: Int = this.text.split(wordSeparator).size
        val words: List<String> = this.text.split(wordSeparator)
    }
}