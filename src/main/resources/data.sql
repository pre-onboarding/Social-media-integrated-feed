insert into post(title,content,type,like_count,share_count,view_count,created_at,modified_at) values
('원티드','원티드 채용 공고 스타트','twitter',10,10,10,CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('멘가고토쿠','라멘집입니다','threads',12,12,12,CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('네이버','경력직 모집해요.경력직 모집해요.경력직 모집해요.경력직 모집해요.','threads',13,13,13,CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP );

insert into hashtag(name,created_at,modified_at) values
('#채용',CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP),
('#원종동',CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP),
('#경력직',CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP);

insert into post_hashtag(content_id,hashtag_id,created_at,modified_at) values
(1,1,CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP),
(2,2,CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP),
(3,1,CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP),
(3,3,CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP);