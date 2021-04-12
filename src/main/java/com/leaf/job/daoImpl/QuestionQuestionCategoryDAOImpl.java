package com.leaf.job.daoImpl;

import com.leaf.job.dao.QuestionQuestionCategoryDAO;
import com.leaf.job.entity.*;
import com.leaf.job.enums.DefaultStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class QuestionQuestionCategoryDAOImpl implements QuestionQuestionCategoryDAO {

	@Autowired
	private EntityManager entityManager;
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuestionQuestionCategoryEntityByQuestion(long questionId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<QuestionQuestionCategoryEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(QuestionQuestionCategoryEntity.class);
        Root<QuestionQuestionCategoryEntity> root = criteriaDelete.from(QuestionQuestionCategoryEntity.class);        
        criteriaDelete.where(
        		criteriaBuilder.equal(root.get(QuestionQuestionCategoryEntity_.questionEntity).get(QuestionEntity_.id),questionId)
		);
        
        entityManager.createQuery(criteriaDelete).executeUpdate();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveQuestionQuestionCategoryEntity(QuestionQuestionCategoryEntity questionQuestionCategoryEntity){
		entityManager.persist(questionQuestionCategoryEntity);
	}

	@Override
	public List<QuestionQuestionCategoryEntity> getQuestionQuestionCategoryEntity(long questionId){

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<QuestionQuestionCategoryEntity> criteriaQuery = criteriaBuilder.createQuery(QuestionQuestionCategoryEntity.class);
		Root<QuestionQuestionCategoryEntity> root = criteriaQuery.from(QuestionQuestionCategoryEntity.class);
		criteriaQuery.select(root);
		criteriaQuery.where(
				criteriaBuilder.and(
						criteriaBuilder.equal(root.get(QuestionQuestionCategoryEntity_.questionCategoryEntity).get(QuestionCategoryEntity_.statusEntity).get(StatusEntity_.code),DefaultStatusEnum.ACTIVE.getCode()),
						criteriaBuilder.equal(root.get(QuestionQuestionCategoryEntity_.questionEntity).get(QuestionEntity_.id),questionId)
				));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public Long getQuestionEntityCountByQuestionCategoryAndStatus(String categoryCode, String statusCode) {
		Long count = 0L;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<QuestionQuestionCategoryEntity> root = criteriaQuery.from(QuestionQuestionCategoryEntity.class);
		criteriaQuery.select(criteriaBuilder.count(root));
		criteriaQuery.where(
				criteriaBuilder.and(
						criteriaBuilder.equal(root.get(QuestionQuestionCategoryEntity_.questionEntity).get(QuestionEntity_.statusEntity).get(StatusEntity_.code),statusCode),
						criteriaBuilder.equal(root.get(QuestionQuestionCategoryEntity_.questionCategoryEntity).get(QuestionCategoryEntity_.code),categoryCode)
				));
		try{
			return entityManager.createQuery(criteriaQuery).getSingleResult();
		}
		catch (Exception e){
			return count;
		}
	}


}
