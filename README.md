# runes
gathering and processing enchanter rune statistics

I'm not to used to publishing/documenting projects but here's an outline:

  EnchanterTradeOffs
  
    -getLeagues
      get tier and ids in league of an id
    -getMatchHistories
      get the games played by accounts in leagues
      in hindsight this could have been a problem if the samples are too small because it's likely to get multiple games from the same player
    -getStats
      get game stats
      ended up only using diamond stats
  RuneStats
    proccess stats for popular primary/secondary rune trees
      create html page
      print graph creation text (copy paste into R)
  RuneGraphs
    print graphs for paths
  RunePathStats
    print graphs for champs
I modified files as I went and unfortunately I'm not familiar with github so I didn't put them in a repository until now.
It turned out sort of like what I had in mind, presentation: https://www.youtube.com/watch?v=OWv1IrI7qP4
If you have any questions, or better yet suggestions for how to do this stuff because I'm sure there was a lot I should have done differently, please email davidmatz@uchicago.edu
