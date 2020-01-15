package mingle.chang.api.controllers

import mingle.chang.api.entities.Guess
import mingle.chang.api.interfaces.GuessRespository
import mingle.chang.api.models.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GuessController  {
    private final var guessRespository: GuessRespository

    @Autowired
    constructor(guessRespository: GuessRespository) {
        this.guessRespository = guessRespository
    }

    @GetMapping("/guess/news")
    fun guessNews(@RequestParam("version") version: String?): Response {
        return try {
            val ver: String = version ?:""
            val guesses = this.guessRespository.findAllByVersionAfter(ver)
            Response(guesses)
        }catch (e: Exception) {
            Response(code = 400, message = e.toString())
        }
    }

    @GetMapping("/guess/list")
    fun guessList(@RequestParam("category") category: String?, @RequestParam("language") language: String?, @PageableDefault(size = 10, page = 0, sort=["modifiedDate"], direction = Direction.DESC) pageable: Pageable) : Response {
        return try {
            val cate: String = category ?:""
            val lan: String = language ?:""
            val guesses = this.guessRespository.findAllByCategoryAndLanguagePageable(cate, lan, pageable)
            Response(guesses)
        }catch (e: Exception) {
            Response(code = 400, message = e.toString())
        }
        this.guessRespository.deleteAll()
    }

    @GetMapping("/guess/categories")
    fun guessCategories(@RequestParam("language") language: String?): Response {
        return try {
            val lan: String = language ?:""
            val result = this.guessRespository.findGroupCategoryByLanguage(lan)
            Response(result)
        }catch (e: Exception) {
            Response(code = 400, message = e.toString())
        }
    }

    @GetMapping("/guess/languages")
    fun guessLanguages(@RequestParam("category") category: String?): Response {
        return try {
            val cate: String = category ?:""
            val result = this.guessRespository.findGroupLanguageByCategory(cate)
            Response(result)
        }catch (e: Exception) {
            Response(code = 400, message = e.toString())
        }
    }

    @PostMapping("/guess/delete")
    fun deleteGuess(@RequestParam("word") word: String, @RequestParam("language") language: String): Response {
        return  try {
            this.guessRespository.deleteByWordAndLanguage(word, language)
            Response()
        }catch (e: Exception) {
            Response(code = 400, message = e.toString())
        }
    }

    @PostMapping("/guess/create")
    fun createGuess(@RequestParam("words") words: String, @RequestParam("category") category: String, @RequestParam("language") language: String): Response {
        return try {
            val wordList = words.split(",")
            var guessList = mutableListOf<Guess>()
            for (word in wordList) {
                val guess = Guess()
                guess.id.word = word
                guess.id.language = language
                guess.category = category
                guessList.add(guess)
            }
            this.guessRespository.saveAll(guessList)
            Response()
        }catch (e: Exception) {
            Response(code = 400, message = e.toString())
        }
    }
}