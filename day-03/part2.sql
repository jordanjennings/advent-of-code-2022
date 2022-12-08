CREATE TABLE rucksacks(sack_content);
.separator ~
.import input.txt rucksacks

WITH
  items AS (
    WITH RECURSIVE split(sack_content, s, last, rest) AS (
      SELECT sack_content, '', '', sack_content FROM rucksacks
      UNION ALL
      SELECT sack_content,
             s || substr(rest, 1, 1),
             substr(rest, 1, 1),
             substr(rest, 2)
      FROM split
      WHERE rest <> ''
    )
    SELECT sack_content, last as item
    FROM split
    WHERE last != ''
  ),
  sacks_with_group AS (
    SELECT sack_content,
           (rowid - 1) / 3 AS group_num,
           (rowid - 1) % 3 AS group_member_num
    FROM rucksacks
  ),
  items_by_group AS (
    SELECT *
    FROM items
    JOIN sacks_with_group using (sack_content)
  ),
  common_item_per_group AS (
    SELECT DISTINCT group_num, item
    FROM items_by_group i1
    JOIN items_by_group i2 USING (group_num, item)
    JOIN items_by_group i3 USING (group_num, item)
    WHERE
      i1.sack_content != i2.sack_content
      AND i2.sack_content != i3.sack_content
      AND i1.sack_content != i3.sack_content
    ORDER BY group_num
  ),
  common_item_priority AS (
    SELECT *,
           iif(item >= 'A' AND item <= 'Z',
               unicode(item) - unicode('A') + 27,
               unicode(item) - unicode('a') + 1) AS priority
    FROM common_item_per_group
  )
SELECT sum(priority) FROM common_item_priority;
