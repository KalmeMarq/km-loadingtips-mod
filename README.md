# KM Loading Tips Mod (Fabric)

Allows you to add tips (and trivia) to loading screens through a json file.

### Example
```json
{
  "enabled": true,
  "loading_tips": {
    "enabled": true,
    "tips": [
      "tips.game.0",
      "tips.game.1",
      "tips.game.2",
      "tips.game.3"
    ]
  }
}
```

### Schema
```
{
  enabled: boolean
  loading_tips: {
    enabled: boolean
    replace: boolean
    tip_area: {
      size: [int | string, int | string]
      max_size: [int | string, int | string]
      min_size: [int | string, int | string]
      offset: [int | string, int | string]
      anchor_from: 'top_left' | 'top_middle' | 'top_right' | 'left_middle' | 'center' | 'right_middle' | 'bottom_left' | 'bottom_middle' | 'bottom_right'
      anchor_to: 'top_left' | 'top_middle' | 'top_right' | 'left_middle' | 'center' | 'right_middle' | 'bottom_left' | 'bottom_middle' | 'bottom_right'
      text_alignment: 'left' | 'center' | 'right'
      space_gap: int
      visible: string[]
      ignored: string[]
    },
    tips: (string | {
      title: string | Text
      tip: string | Text
      visible: string[]
      ignored: string[]
    })[]
  }
  loading_trivia: {
    enabled: boolean
    replace: boolean
    trivia_area: {
      size: [int | string, int | string]
      max_size: [int | string, int | string]
      min_size: [int | string, int | string]
      offset: [int | string, int | string]
      anchor_from: 'top_left' | 'top_middle' | 'top_right' | 'left_middle' | 'center' | 'right_middle' | 'bottom_left' | 'bottom_middle' | 'bottom_right'
      anchor_to: 'top_left' | 'top_middle' | 'top_right' | 'left_middle' | 'center' | 'right_middle' | 'bottom_left' | 'bottom_middle' | 'bottom_right'
      text_alignment: 'left' | 'center' | 'right'
      space_gap: int
      visible: string[]
      ignored: string[]
    },
    trivia: (string | {
      title: string | Text
      tip: string | Text
      visible: string[]
      ignored: string[]
    })[]
  }
}
```